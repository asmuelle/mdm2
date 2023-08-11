import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IOwnershipClassification } from '../ownership-classification.model';
import { OwnershipClassificationService } from '../service/ownership-classification.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './ownership-classification-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OwnershipClassificationDeleteDialogComponent {
  ownershipClassification?: IOwnershipClassification;

  constructor(protected ownershipClassificationService: OwnershipClassificationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ownershipClassificationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
