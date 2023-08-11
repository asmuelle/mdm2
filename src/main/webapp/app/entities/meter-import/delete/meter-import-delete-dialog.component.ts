import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IMeterImport } from '../meter-import.model';
import { MeterImportService } from '../service/meter-import.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './meter-import-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MeterImportDeleteDialogComponent {
  meterImport?: IMeterImport;

  constructor(protected meterImportService: MeterImportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.meterImportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
